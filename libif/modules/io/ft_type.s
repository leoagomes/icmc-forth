; ( address n -- )
; prints n chars of the string at address
ft_type:
	push r0
	push r1
	push r2
	push r3
	push r4
	push r5

	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	xor r2, r2, r2
	load r3, FT_CONSOLE_CURSOR_POS
	load r4, FT_CONSOLE_COLOR

ft_type_loop:
	cmp r1, r2
	jeq ft_type_loop_end
	loadi r5, r0
	add r5, r5, r4
	outchar r5, r3
	inc r0
	dec r1
	inc r3
	jmp ft_type_loop

ft_type_loop_end:
	store FT_CONSOLE_CURSOR_POS, r3
	pop r5
	pop r4
	pop r3
	pop r2
	pop r1
	pop r0
	rts