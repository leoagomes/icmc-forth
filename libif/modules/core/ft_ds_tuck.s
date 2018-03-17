; ( a b -- b a b )
ft_ds_tuck:
	call ft_ds_swap
	call ft_ds_over
	rts