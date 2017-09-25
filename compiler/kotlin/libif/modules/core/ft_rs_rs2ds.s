; ( -- a ) { a -- }
ft_rs_rs2ds:
	push r0
	call ft_rs_pop
	call ft_ds_push
	pop r0
	rts