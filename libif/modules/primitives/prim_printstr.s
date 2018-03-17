; r0 : str
prim_printstr:
	push r0
	push r1
	push r2
	push r3
	push r4

	load r4, FT_CONSOLE_COLOR
	xor r2, r2, r2
	load r1, FT_CONSOLE_CURSOR_POS

prim_printstr_loop:
	loadi r3, r0
	inc r1
	cmp r3, r2
	jeq prim_printstr_loop_end
	add r3, r3, r4             ; add color to char
	outchar r3, r1
	inc r0
	jmp prim_printstr_loop

prim_printstr_loop_end:
    store FT_CONSOLE_CURSOR_POS, r1
	pop r4
	pop r3
	pop r2
	pop r1
	pop r0
	rts