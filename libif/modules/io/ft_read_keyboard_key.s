; ( -- a )
ft_read_keyboard_key:
	push r0
	inchar r0
	call ft_ds_push
	pop r0
	rts