//-----------------------------------------------------
// Design Name : And
// File Name   : And.v
// Function    : And chip (parameterized)
// Coder       : Jack Rollinson
//-----------------------------------------------------
module And #(parameter WIDTH = 1)(input[WIDTH-1:0] a, input[WIDTH-1:0] b,  output[WIDTH-1:0] out);

//-------------Code Starts Here---------

	assign out = a & b;

//-------------Code Ends Here-----------

endmodule //End Of Module And
