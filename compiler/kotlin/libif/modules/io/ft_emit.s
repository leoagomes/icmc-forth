; ( a -- )
ft_emit:
	push r0
	push r1
	push r2
	call ft_ds_pop
	load r2, FT_CONSOLE_COLOR
	load r1, FT_CONSOLE_CURSOR_POS
	inc r1
	store FT_CONSOLE_CURSOR_POS, r1
	outchar r0, r1
	pop r2
	pop r1
	pop r0
	rts