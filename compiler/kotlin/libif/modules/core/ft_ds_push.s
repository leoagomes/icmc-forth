; r0 -- data to push
; ( -- <r0> )
ft_ds_push:
	storei r7, r0
	inc r7
	rts