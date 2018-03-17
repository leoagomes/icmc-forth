ft_dot:
	push r0

	call ft_ds_pop
	call prim_printno
	load r0, FT_CONSOLE_CURSOR_POS
	inc r0
	store FT_CONSOLE_CURSOR_POS, r0

	pop r0
	rts
