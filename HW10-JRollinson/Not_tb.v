//-----------------------------------------------------
// Design Name : Not Testbench
// File Name   : Not_tb.v
// Function    : Testbench for Not gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module Not_tb;

	/*VARIABLE SETUP */
	reg t_in; 
	wire t_out; 

	//create chip instance and connect it
	Not n1 (.in(t_in), .out(t_out));


	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 


	/* TEST VALUES 
	#1 means wait one timestep */
	initial begin
	
		t_in = 1'b0;
		
		#1 
		
		t_in = 1'b1;

		#1 $stop;
	end 


	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("|  in | out |"); 
		$monitor("|  %b  |  %b  |",t_in, t_out); 
	end 

endmodule 
