prim_resetcursor:
	push r0
	xor r0, r0, r0
	store FT_CONSOLE_CURSOR_POS, r0
	pop r0
	rts
