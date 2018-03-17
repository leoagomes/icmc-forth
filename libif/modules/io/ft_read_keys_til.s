; ( char -- straddr)
ft_read_keys_til:
	push r0
	push r1
	push r2
	push r3
	push r4

	call ft_ds_pop
	loadn r1, #FT_KBD_BUFFER
	loadn r2, #512
	xor r3, r3, r3

ft_read_keys_til_rl:
	inchar r4
	cmp r4, r3
	jeq ft_read_keys_til_rl
	storei r1, r4
	inc r1
	cmp r0, r4
	jne ft_read_keys_til_rl

	storei r1, r3
	loadn r0, #FT_KBD_BUFFER
	call ft_ds_push

	pop r4
	pop r3
	pop r2
	pop r1
	pop r0
	rts