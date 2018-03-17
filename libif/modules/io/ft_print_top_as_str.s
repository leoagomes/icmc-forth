; ( addrs -- )
ft_print_top_as_str:
	push r0
	call ft_ds_pop
	call prim_printstr
	pop r0
	rts