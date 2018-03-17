ft_greater:
	push r0
	push r1
	
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	cmp r0, r1
	jel ft_greater
	loadn r0, #1
	jmp ft_greater_end

ftgreater_push0:
	xor r0, r0, r0

ft_greater_end:
	call ft_ds_push
	pop r1
	pop r0
	rts