//-----------------------------------------------------
// Design Name : Xor
// File Name   : Xor.v
// Function    : Xor chip (parameterized)
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Xor #(parameter WIDTH = 1)( input[WIDTH-1:0] a, input[WIDTH-1:0] b,  output[WIDTH-1:0] out); 

//-------------Code Starts Here---------

	assign out = a ^ b;

//-------------Code Ends Here---------

endmodule //End Of Module Xor
