jmp main

; R7 -- DSP
; R6 -- RSP

var FT_DATA_STACK_BEGIN: var #128
var FT_RETURN_STACK_BEGIN: var #128

; r0: address to call
ft_exec:
	push r1
	loadn r1, #ft_callstb
	inc r1
	storei r1, r0
ft_callstb:
	call ft_callstb.emptyret
	pop r1
ft_callstb.emptyret:
	rts

; r0: address to jump to
ft_branch:
	push r1
	loadn r1, #ft_jmpstb
	inc r1
	storei r1, r0
ft_jmpstb:
	jmp ft_jmpstb.justafter
ft_jmpstb.justafter:
	pop r1
	rts

; r0 -- data to push
; ( -- <r0> )
ft_ds_push:
	storei r7, r0
	inc r7
	rts

; r0 -- data popped
; ( a -- ) r0 = a
ft_ds_pop:
	dec r7
	loadi r0, r7
	rts

; ( a -- )
ft_ds_drop:
	dec r7
	rts

; ( a -- a a )
ft_ds_dup:
	push r0
	push r1
	mov r1, r7
	dec r1
	loadi r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts

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

; r0 -- data to push
ft_rs_push:
	storei r6, r0
	inc r6
	rts

; r0 -- data popped
ft_rs_pop:
	dec r6
	loadi r0, r6
	rts

; ( address -- x )
ft_peek:
	push r0
	call ft_ds_pop
	loadi r0, r0
	call ft_ds_push
	pop r0
	rts
	
; ( x address -- )
ft_poke:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	storei r1, r0
	pop r1
	pop r0
	rts
	
; if top of stack is zero will branch to r0
ft_zbranch:
	push r0
	push r1
	push r2
	push r3
	mov r3, r0
	loadn r2, #0
	call ft_ds_pop
	mov r1, r0
	cmp r1, r2
	jne ft_zbranch.out
	mov r0, r3
	call ft_branch
	ft_zbranch.out:
	pop r3
	pop r2
	pop r1
	pop r0
	rts

; not sure this should be useful
ft_exit:
	dec r6
	loadi r0, r6
	pop r1
	push r0
	rts
	
; ( a b -- c ); c = a + b
ft_add:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	add r0, r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts

; ( a b -- c ); c = a - b	
ft_sub:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	sub r0, r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts

ft_mul:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	mul r0, r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts
	
ft_div:
	push r0
	push r1
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	div r0, r0, r1
	call ft_ds_push
	pop r1
	pop r0
	rts

ft_setup:
	loadn r7, #FT_DATA_STACK_BEGIN
	loadn r6, #FT_RETURN_STACK_BEGIN
	rts

main:
	call ft_setup
	halt