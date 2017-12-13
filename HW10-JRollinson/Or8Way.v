//-----------------------------------------------------
// Design Name : Or8Way
// File Name   : Or8Way.v
// Function    : Or8Way chip (parameterized)
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Or8Way (input[7:0] in, output out); 

//-------------Code Starts Here---------

	wire t_a, t_b, t_c, t_d, t_e, t_f, t_g, t_h;
	
	assign t_a = in[0];
	assign t_b = in[1];
	assign t_c = in[2];
	assign t_d = in[3];
	assign t_e = in[4];
	assign t_f = in[5];
	assign t_g = in[6];
	assign t_h = in[7];

	assign out = ((t_a | t_b) | (t_c | t_d)) | 	((t_e | t_f) | (t_g | t_h));
		
//-------------Code Ends Here---------

endmodule //End Of Module Or8Way
