prim_clearscreen:
	push r0
	push r1
	push r2

	xor r0, r0, r0
	loadn r1, #' '
	loadn r2, #1200

prim_clearscreen_lp:
	outchar r1, r0
	inc r0
	cmp r0, r2
	jle prim_clearscreen_lp

	pop r2
	pop r1
	pop r0
	rts
