; r0 -- data popped
; ( a -- ) r0 = a
ft_ds_pop:
	dec r7
	loadi r0, r7
	rts