ft_or:
	push r0
	push r1
	
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	or r0, r1, r0
	call ft_ds_push

	pop r1
	pop r0
	rts