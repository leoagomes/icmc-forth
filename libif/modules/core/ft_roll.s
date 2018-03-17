; ( a0 ... an n -- a1 ... an a0 )
ft_roll:
	push r0
	push r1
	push r2
	push r3

	call ft_ds_pop
	mov r1, r7
	dec r1
	sub r1, r1, r0
	loadi r3, r1
	mov r2, r1
	inc r1

ft_roll_loop:
	storei r2, r1
	inc r2
	inc r1
	cmp r1, r7
	jne ft_roll_loop

	dec r1
	storei r1, r3

	pop r3
	pop r2
	pop r1
	pop r0
	rts