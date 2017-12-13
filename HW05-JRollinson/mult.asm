// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm
// Jack Rollinson, 9/27/2017

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here:

@sum   // value @sum, is now stored in register A.
M=0    // RAM[@sum] is now 0.
@i	   // value @i, is now stored in register A.
M=1    // RAM[@i] is now 1.

(LOOP) // Start of the loop.
@i     // value @i, is now stored in register A.
D=M    // register D is now holding the value RAM[@i].
@R0    // register A is now holding the value @R0.
D=D-M  // register D is now holding the value of @i - RAM[@R0].
@END   // A is now equal to the value @END.
D;JGT  // if(@i-@R0) > 0 jump to (END).

@R1	   // A = @R1.
D=M    // D = RAM[@R1].
@sum   // A = @sum.
M=D+M  // RAM[@sum] += RAM[@R1].
@i	   // A = @i.
M=M+1  // RAM[@i]++.
@LOOP  // A = @LOOP.
0;JMP  // jump to (LOOP).

(END)
@sum   // A = @sum.
D=M	   // D = RAM[@sum].
@R2    // A = @R2.
M=D    // RAM[@R2] = RAM[@sum].