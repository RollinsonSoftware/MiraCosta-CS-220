//-----------------------------------------------------
// Design Name : Not16 Testbench
// File Name   : Not16_tb.v
// Function    : Testbench for Not16 gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Not16_tb;

	/*VARIABLE SETUP */
	reg	[15:0]	t_in; 
	wire[15:0]	t_out; 

	//create chip instance and connect it
	Not#(16) n1(.in(t_in), .out(t_out));

	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 

	/* TEST VALUES 
	#1 means wait one timestep */
	initial begin
	
		t_in = 16'b0000_0000_0000_0000;
		
		#1 
		
		t_in = 16'b1111_1111_1111_1111;
		
		#1
		 
		t_in = 16'b1010_1010_1010_1010;
		
		#1
		 
		t_in = 16'b0011_1100_1100_0011;
		
		#1 
		
		t_in = 16'b0001_0010_0011_0100;
		
		#1 $stop;
	end 

	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("| %16s | %16s |","in","out"); 
		$monitor("| %16b | %16b |", t_in, t_out); 
	end 

endmodule //End of Not16_tb
