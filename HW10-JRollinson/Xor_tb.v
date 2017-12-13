//-----------------------------------------------------
// Design Name : Xor Testbench
// File Name   : Xor.v
// Function    : Testbench for Xor gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Xor_tb;

	/*VARIABLE SETUP */
	reg t_a, t_b; 
	wire t_out; 

	//create chip instance and connect it
	Xor n1 (.a(t_a), .b(t_b), .out(t_out));

	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 

	/* TEST VALUES 
	#1 means wait one timestep */
	initial begin
	
		t_a = 1'b0;
		t_b = 1'b0;
		
		#1 
		
		t_b = 1'b1;
		
		#1 
		
		t_b = 1'b0;
		t_a = 1'b1;
		
		#1 
		
		t_b = 1'b1;
		
		#1 $stop;
	end 

	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("| a   |  b  | out |"); 
		$monitor("|  %b  |  %b  |  %b  |", t_a, t_b, t_out); 
	end 

endmodule //End of Xor_tb
