; ( a b -- b a )
ft_ds_swap:
	push r0
	push r1
	push r2
	mov r1, r7
	dec r1
	loadi r0, r1
	dec r1
	loadi r2, r1
	storei r1, r0
	inc r1
	storei r1, r2
	pop r2
	pop r1
	pop r0
	rts
