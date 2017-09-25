; ( a b -- c ) ; c = a % b
ft_mod:
	push r0
	push r1
	
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	mod r0, r0, r1
	call ft_ds_push
	
	pop r0
	pop r1
	rts