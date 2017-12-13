//-----------------------------------------------------
// Design Name : And Testbench
// File Name   : And_tb.v
// Function    : Testbench for And gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module And_tb;

	/*VARIABLE SETUP */
	reg t_a, t_b; //inputs are registers
	wire test_out; //outputs are wires, one bit default

	//create chip instance and connect it
	And n1 (.a(t_a), .b(t_b), .out(test_out));

	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 

	/* TEST VALUES 
	# 1 means wait one timestep */
	initial begin

		$monitor(t_a, t_b, test_out);

		t_a = 1'b0;
		t_b = 1'b0;

		#1
		
		t_a = 1'b0;
		t_b = 1'b1;

		#1 
		
		t_a = 1'b1;
		t_b = 1'b0;

		#1
		
		t_a = 1'b1;
		t_b = 1'b1;
		
		#1 $stop;

	end

	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("| a   |  b  | out |"); 
		$monitor("|  %b  |  %b  |  %b  |", t_a, t_b, test_out); 
	end
	
endmodule
