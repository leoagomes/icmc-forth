ft_equals:
	push r0
	push r1
	
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	cmp r1, r0
	jne ft_equals_push0
	loadn r0, #1
	jmp ft_equals_end
ft_equals_push0:
	xor r0, r0, r0

ft_equals_end:
	call ft_ds_push
	pop r1
	pop r0
	rts