//-----------------------------------------------------
// Design Name : Not
// File Name   : Not.v
// Function    : Not chip (parameterized)
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Not #(parameter WIDTH = 1)( input[WIDTH-1:0] in, output[WIDTH-1:0] out); 

//-------------Code Starts Here---------

	assign out = ~in;

//-------------Code Ends Here-----------

endmodule //End Of Module Not
