//-----------------------------------------------------
// Design Name : Or8way Testbench
// File Name   : Or8way_tb.v
// Function    : Testbench for Or8way gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Or8Way_tb;

	/*VARIABLE SETUP */
	
	reg	[7:0] t_in; 
	wire t_out; 

	//create chip instance and connect it
	Or8Way n1(.in(t_in), .out(t_out));

	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 

	/* TEST VALUES 
	#1 means wait one timestep */
	initial begin
	
		t_in = 8'b0000_0000;
		
		#1 
		
		t_in = 8'b1111_1111;
		
		#1 
		
		t_in = 8'b0001_0000;
		
		#1 
		
		t_in = 8'b0000_0001;
		
		#1 
		
		t_in = 8'b0010_0110;
		
		#1 $stop;
	end 

	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("| %8s | %2s|","in","out"); 
		$monitor("| %8b | %2b |",t_in, t_out); 
	end 

endmodule //End of Or8Way_tb
