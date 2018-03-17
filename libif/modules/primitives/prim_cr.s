prim_cr:
	push r0
	push r1
	push r2

	loadn r1, #40
	load r0, FT_CONSOLE_CURSOR_POS
	div r2, r0, r1
	inc r2
	mul r2, r2, r1
	loadn r0, #1200
	mod r2, r2, r0
	store FT_CONSOLE_CURSOR_POS, r2

	pop r2
	pop r1
	pop r0
	rts
