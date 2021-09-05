--
--  Framework: Uwe R. Zimmer, Australia, 2019
--
with Exceptions; use Exceptions;

package body Generic_Router is

   task body Router_Task is

      Connected_Routers : Ids_To_Links;

   begin
      accept Configure (Links : Ids_To_Links) do
         Connected_Routers := Links;
      end Configure;

      declare
         Port_List : constant Connected_Router_Ports := To_Router_Ports (Task_Id, Connected_Routers);
         RBS       : Router_tables;
         Is_change : Boolean := False;
         pragma Unreferenced (Is_change);
         M         : Message_unit;
         MQ        : Ms;                      -----   result queue(modified queue by array), store messages.
         qtail     : sf := 0;               -----   queue tail
         qhead     : sf := 0;               -----   queue head
         Is_shut   : Boolean := False;
         task rt;
         task body rt is
         begin
            while not Is_shut loop     -- when the router is not shut down, it will loop forever to send its routertables to other routers in time
               --  update routertable in time
               delay 0.001;
               for i in 1 .. Port_List'Length loop
                  select
                     Port_List (i).Link.all.Receive_Routertables (RBS, Task_Id);
                  or
                     delay 0.001;
                  end select;
               end loop;
            end loop;
         end rt;

      begin

         --  Replace the following dummy code with the code of your router.
         --  None of the following code structures make necessarily any sense,
         --  so feel free to delete in full and rewrite from scratch.
         --  You will still need to handle all defined entries and will need to
         --  use the exisitng ports in your own code.

         --  initialize the routertables.
         for i in 1 .. Router_Range'Last loop
            RBS (i).distance := 0;
            RBS (i).Router_det_Id := Router_Range'Invalid_Value;
            RBS (i).Router_port_id := Router_Range'Invalid_Value;
            RBS (i).Is_direct := True;
         end loop;
         --  all routers know the situation about their neighbour
         for i in 1 .. Port_List'Length loop
            RBS (Port_List (i).Id).Router_det_Id := Port_List (i).Id;
            RBS (Port_List (i).Id).Router_port_id := Port_List (i).Id;
            RBS (Port_List (i).Id).distance  := 1;
            RBS (Port_List (i).Id).Is_direct := True;
         end loop;
         Is_change := True;
         loop
            select

               accept Receive_Routertables (PRB : in Router_tables; sender : in Router_Range) do -- the logic of changing routertables.
                  declare
                     NRB : constant Router_tables := PRB;
                     sd : constant Router_Range := sender;
                  begin
                     for i in 1 .. Router_Range'Last loop
                        if NRB (i).Router_det_Id'Valid then              --  choose valid rows in NRB
                           if not RBS (i).Router_det_Id'Valid then                 --  if the row in NRB is valid but not valid in mine, copy it into my routertables
                              RBS (i).Router_det_Id := NRB (i).Router_det_Id;
                              RBS (i).Router_port_id := sd;                         -----  the port will be changed as the sender.
                              RBS (i).distance := NRB (i).distance + 1;              -----and add 1 to distance.
                              RBS (i).Is_direct := False;
                              Is_change := True;
                           else
                              if RBS (i).distance > NRB (i).distance + 1 then     --- if reach destination by NRB's router is faster than by my self, copy it into my routertables
                                 RBS (i).Router_det_Id := NRB (i).Router_det_Id;       -----  the port will be changed as the sender.
                                 RBS (i).distance := NRB (i).distance + 1;          -----and add 1 to distance.
                                 RBS (i).Router_port_id := sd;
                                 RBS (i).Is_direct := False;
                                 Is_change := True;
                              end if;
                           end if;
                        end if;
                     end loop;
                  end;
               end Receive_Routertables;
            or
               accept Send_Message (Message : in Messages_Client) do
                  declare
                     Swallow_Message : constant Messages_Client := Message;
                  begin
                       M.Orignal_Destination := Swallow_Message.Destination;
                       M.Orignal_sendder := Task_Id;
                       M.Hop_Counter := 0;
                       M.sender := Task_Id;
                       if RBS (M.Orignal_Destination).Router_det_Id'Valid then   -- whether the destination is in my routertables.
                          M.destination := RBS (M.Orignal_Destination).Router_port_id;
                          if Task_Id /= Swallow_Message.Destination then   -- whether the destination is me
                             for i in 1 .. Port_List'Length loop
                                if Port_List (i).Id = M.destination then
                                   Port_List (i).Link.all.Receive_unit (M);
                                end if;
                             end loop;
                          else                         ----- yes, is me
                           MQ (qtail) := M;            -- put the message into my result queue.
                           qtail := qtail + 1;
                          end if;
                       end if;
                  end;
               end Send_Message;
            or
               when not (qhead = qtail) => accept Receive_Message (Message : out Messages_Mailbox) do --- when my queue is not empty, recieve it
                  begin
                     Message.Sender := MQ (qhead).Orignal_sendder;
                     Message.The_Message := MQ (qhead).Message;
                     Message.Hop_Counter := MQ (qhead).Hop_Counter;
                     qhead := qhead + 1;
                  end;
               end Receive_Message;
            or
               accept Receive_unit (Me : in Message_unit) do   -- send message unit
                  declare
                     Mn : constant Message_unit := Me;
                  begin
                       M.Orignal_Destination := Mn.Orignal_Destination;
                       M.Orignal_sendder := Mn.Orignal_sendder;
                       M.sender := Task_Id;
                       M.Message := Mn.Message;
                      M.Hop_Counter := Mn.Hop_Counter + 1;
                       if RBS (M.Orignal_Destination).Router_det_Id'Valid then              -- whether the destination is in my routertables.
                          M.destination := RBS (M.Orignal_Destination).Router_port_id;
                          if Task_Id /= M.Orignal_Destination then            -- whether the destination is me
                             for i in 1 .. Port_List'Length loop
                                if Port_List (i).Id = M.destination then
                                   Port_List (i).Link.all.Receive_unit (M);
                                end if;
                             end loop;
                          else                                                -- yes, is me
                           MQ (qtail) := M;                                  -- put the message into my result queue.
                           qtail := qtail + 1;
                          end if;

                       end if;
                  end;
               end Receive_unit;
            or
               accept Shutdown;
               Is_shut := True;
               exit;
            end select;

         end loop;

         for Port of Port_List loop
            null;
         end loop;

      end;

   exception
      when Exception_Id : others => Show_Exception (Exception_Id);

   end Router_Task;

end Generic_Router;
