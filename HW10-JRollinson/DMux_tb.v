//-----------------------------------------------------
// Design Name : DMux Testbench
// File Name   : DMux_tb.v
// Function    : Testbench for DMux gate
// Coder       : Jack Rollinson
//-----------------------------------------------------
module DMux_tb;

	/*VARIABLE SETUP */
	reg t_in, t_sel;
	wire t_a, t_b; 

	//create chip instance and connect it
	DMux n1(.in(t_in), .sel(t_sel), .a(t_a), .b(t_b));

	/* PULSING CLOCK FOR TEST
	(changes phase after each timestep) */
	reg clk = 0;
	always #1 clk = !clk; 

	/* TEST VALUES 
	#1 means wait one timestep */
	initial begin
	
		t_in = 1'b0;
		t_sel = 1'b0;
		
		#1 
		
		t_sel = 1'b1;
		
		#1 
		
		t_in = 1'b1;
		t_sel = 1'b0;
		
		#1 
		
		t_sel = 1'b1;
		
		#1 $stop; 
	end 


	/* RUN TEST */
	//display happens only once
	//monitor runs whenever variables change
	initial begin
		$display("|  in |  sel|  a  |  b  |"); 
		$monitor("|  %b  |  %b  |  %b  |  %b  |", t_in, t_sel, t_a, t_b); 
	end 

endmodule
