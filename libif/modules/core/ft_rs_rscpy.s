; ( -- a ) { a -- a }
ft_rs_rscpy:
	push r0
	push r1
	mov r1, r6
	dec r1
	loadi r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts