prim_clearline:
	push r0
	push r1
	push r2

	load r0, FT_CONSOLE_CURSOR_POS
	loadn r1, #40
	div r2, r0, r1
	mul r2, r2, r1
	add r1, r1, r2
	loadn r0, #' '

prim_clearline_lp:
	outchar r0, r2
	inc r2
	cmp r2, r1
	jle prim_clearline_lp

	pop r0
	pop r1
	pop r2
	rts
