FT_NUMSTR_BUFFER: var #5
FT_NUMSTR_BUFFER_END: var #2

; r0 = no
prim_printno:
	push r0
	push r1
	push r2
	push r3
	push r4
	push r5
	push r6

	loadn r6, #'0'
	xor r5, r5, r5
	loadn r3, #10
	loadn r1, #FT_NUMSTR_BUFFER
	loadn r2, #FT_NUMSTR_BUFFER_END

prim_printno_loop:
	mod r4, r0, r3
	add r4, r4, r6
	storei r2, r4
	dec r2
	div r0, r0, r3
	cmp r0, r5
	jne prim_printno_loop

	inc r2
	mov r0, r2

prim_printno_fsl_end:
	call prim_printstr

	pop r6
	pop r5
	pop r4
	pop r3
	pop r2
	pop r1
	pop r0
	rts