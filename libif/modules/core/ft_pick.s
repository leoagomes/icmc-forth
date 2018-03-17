; ( a0 ... an n -- a0 ... an a0 )
ft_pick:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r7
	dec r1
	sub r1, r1, r0
	loadi r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts