//-----------------------------------------------------
// Design Name : And16 Testbench
// File Name   : And16_tb.v
// Function    : Testbench for And16 gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module And16_tb;

	/*VARIABLE SETUP */
	reg	[15:0]	t_a; 
	reg	[15:0]	t_b; 
	wire[15:0]	test_out; 

	//create chip instance and connect it
	And#(16) n1(.a(t_a), .b(t_b), .out(test_out));


	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 


	/* TEST VALUES 
	#1 means wait one timestep */
	initial begin
	
		t_a = 16'b0000_0000_0000_0000;
		t_b = 16'b0000_0000_0000_0000;
	
		#1 
		
		t_b = 16'b1111_1111_1111_1111;
		
		#1 
		
		t_a = 16'b1111_1111_1111_1111;
		
		#1 
		
		t_a = 16'b1010_1010_1010_1010;
		t_b = 16'b0101_0101_0101_0101;
			
		#1 
		
		t_a = 16'b0011_1100_1100_0011;
		t_b = 16'b0000_1111_1111_0000;
			
		#1 
		
		t_a = 16'b0001_0010_0011_0100;
		t_b = 16'b1001_1000_0111_0110;
		
		#1 $stop;
	end 


	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("| %16s   | %16s   | %16s   |","a","b","out"); 
		$monitor("|  %b  |  %b  |  %b  |", t_a, t_b, test_out); 
	end 

endmodule 
