; ( a b -- a b a )
ft_ds_over:
	push r0
	push r1
	mov r1, r7
	dec r1
	dec r1
	loadi r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts