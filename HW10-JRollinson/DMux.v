//-----------------------------------------------------
// Design Name : DMux
// File Name   : DMux.v
// Function    : DMux chip (parameterized)
// Coder       : Jack Rollinson
//-----------------------------------------------------
module DMux (input in, input sel, output a, output b); 

//-------------Code Starts Here---------

	reg a, b;
	
	always@(*)
	begin
		a = 1'd0;
		b = 1'd0;
		
		if (sel == 1'd0)
				a = in;
		else
				b = in;
	end 

//-------------Code Ends Here---------

endmodule //End Of Module DMux
