; ( address -- x )
ft_peek:
	push r0
	call ft_ds_pop
	loadi r0, r0
	call ft_ds_push
	pop r0
	rts