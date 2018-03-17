; ( x address -- )
ft_poke:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	storei r1, r0
	pop r1
	pop r0
	rts