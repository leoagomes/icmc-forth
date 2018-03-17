; ( a b -- b )
ft_ds_nip:
	push r0
	push r1
	dec r7
	mov r1, r7
	loadi r0, r1
	dec r1
	storei r1, r0
	pop r1
	pop r0
	rts