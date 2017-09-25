; ( a b -- c ); c = a / b
ft_div:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	div r0, r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts