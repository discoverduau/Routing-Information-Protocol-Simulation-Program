--
--  Framework: Uwe R. Zimmer, Australia, 2015
--

with Ada.Strings.Bounded;           use Ada.Strings.Bounded;
with Generic_Routers_Configuration;

generic
   with package Routers_Configuration is new Generic_Routers_Configuration (<>);

package Generic_Message_Structures is

   use Routers_Configuration;

   package Message_Strings is new Generic_Bounded_Length (Max => 80);
   use Message_Strings;

   subtype The_Core_Message is Bounded_String;

   type Messages_Client is record
      Destination : Router_Range;
      The_Message : The_Core_Message;
   end record;

   type Messages_Mailbox is record
      Sender      : Router_Range     := Router_Range'Invalid_Value;
      The_Message : The_Core_Message := Message_Strings.To_Bounded_String ("");
      Hop_Counter : Natural          := 0;
   end record;

   -- Leave anything above this line as it will be used by the testing framework
   -- to communicate with your router.

   --  Add one or multiple more messages formats here ..

   type Router_table is record
      distance   : Natural;
      Router_det_Id  : Router_Range;
      Router_port_id : Router_Range;
      Is_direct  : Boolean;
   end record;

   type Message_unit is record
      Orignal_sendder : Router_Range := Router_Range'Invalid_Value;
      Orignal_Destination : Router_Range := Router_Range'Invalid_Value;
      sender            : Router_Range := Router_Range'Invalid_Value;
      destination       : Router_Range := Router_Range'Invalid_Value;
      Message           : The_Core_Message;
      Hop_Counter : Natural          := 0;
   end record;

   type Router_tables is array (1 .. Router_Range'Last) of Router_table;
   type sf is mod 5000;
   type Ms            is array (sf) of Message_unit;                   ------ using array to modify a shabby queue

end Generic_Message_Structures;
