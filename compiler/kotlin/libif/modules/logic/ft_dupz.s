ft_dupz:
	push r0
	push r1
	
	mov r1, r7
	dec r1
	loadi r0, r1
	cmp r0, r0
	jz ft_dupz_end
	call ft_ds_push
	
ft_dupz_end:
	pop r1
	pop r0
	rts