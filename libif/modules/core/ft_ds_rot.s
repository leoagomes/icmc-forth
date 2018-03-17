; ( a b c -- b c a)
ft_ds_rot:
	push r0
	push r1
	push r2
	push r3
	mov r3, r7
	dec r3
	loadi r0, r3 ; r0 = c
	dec r3
	loadi r1, r3 ; r1 = b
	dec r3
	loadi r2, r3 ; r2 = a
	storei r3, r1
	inc r3
	storei r3, r0
	inc r3
	storei r3, r2
	pop r3
	pop r2
	pop r1
	pop r0
	rts