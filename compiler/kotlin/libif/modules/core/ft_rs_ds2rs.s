; ( a -- ) { -- a }
ft_rs_ds2rs:
	push r0
	call ft_ds_pop
	call ft_rs_push
	pop r0
	rts