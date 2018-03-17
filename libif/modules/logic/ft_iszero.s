ft_iszero:
	push r0
	
	call ft_ds_pop
	cmp r0, r0
	jnz ft_ds_push0
	loadn r0, #1
	jmp ft_iszero_end

ft_iszero_push0:
	xor r0, r0, r0

ft_iszero_end:
	call ft_ds_push
	pop r0
	rts