jmp main

; R7 -- DSP
; R6 -- RSP

FT_DATA_STACK_BEGIN: var #128
FT_RETURN_STACK_BEGIN: var #128

FT_CONSOLE_CURSOR_POS: var #1

FT_NUMSTR_BUFFER: var #5
FT_NUMSTR_BUFFER_END: var #2

; r0: address to call
ft_exec:
	push r1
	loadn r1, #ft_callstb
	inc r1
	storei r1, r0
ft_callstb:
	call ft_callstb_emptyret
	pop r1
ft_callstb_emptyret:
	rts

; r0: address to jump to
ft_branch:
	push r1
	loadn r1, #ft_jmpstb
	inc r1
	storei r1, r0
ft_jmpstb:
	jmp ft_jmpstb_justafter
ft_jmpstb_justafter:
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

; ( a b -- b a b )
ft_ds_tuck:
	call ft_ds_swap
	call ft_ds_over
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

; ( a -- ) { -- a }
ft_rs_ds2rs:
	push r0
	call ft_ds_pop
	call ft_rs_push
	pop r0
	rts

; ( -- a ) { a -- }
ft_rs_rs2ds:
	push r0
	call ft_rs_pop
	call ft_ds_push
	pop r0
	rts

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
	xor r2, r2, r2 ; r2 = 0
	call ft_ds_pop
	mov r1, r0
	cmp r1, r2
	jne ft_zbranch_out
	mov r0, r3
	call ft_branch
	ft_zbranch_out:
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

; ( a b -- c ); c = a * b
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

; ( a b -- c ); c = a / b
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

; r0 : str
prim_printstr:
	push r0
	push r1
	push r2
	push r3

	xor r2, r2, r2
	load r1, FT_CONSOLE_CURSOR_POS

prim_printstr_loop:
	loadi r3, r0
	cmp r3, r2
	jeq prim_printstr_loop_end
	outchar r3, r1
	inc r1
	inc r0
	jmp prim_printstr_loop

prim_printstr_loop_end:
	pop r3
	pop r2
	pop r1
	pop r0
	rts

; r0 = no
prim_printno:
	push r0
	push r1
	push r2
	push r3
	push r4
	push r5
	push r6

	loadn r6, #'0'
	xor r5, r5, r5
	loadn r3, #10
	loadn r1, #FT_NUMSTR_BUFFER
	loadn r2, #FT_NUMSTR_BUFFER_END

prim_printno_loop:
	mod r4, r0, r3
	add r4, r4, r6
	storei r2, r4
	dec r2
	div r4, r0, r3
	cmp r4, r5
	jne prim_printno_loop
	
	loadn r0, #FT_NUMSTR_BUFFER
prim_printno_fsl:
	loadi r1, r0
	cmp r1, r6
	jne prim_printno_fsl_end
	inc r0
	jmp prim_printno_fsl

prim_printno_fsl_end:
	call prim_printstr

	pop r6
	pop r5
	pop r4
	pop r3
	pop r2
	pop r1
	pop r0
	rts

ft_print_stack:
	push r0
	push r1
	push r2
	push r3

	mov r1, r7
	loadn r2, #FT_DATA_STACK_BEGIN

	cmp r1, r2
	jeq ft_print_stack_e
	
ft_print_stack_popl:
	dec r1
	loadi r0, r1
	call prim_printno
	load r3, FT_CONSOLE_CURSOR_POS
	inc r3
	store FT_CONSOLE_CURSOR_POS, r3
	cmp r1, r2
	jne ft_print_stack_popl

ft_print_stack_e:
	pop r3
	pop r2
	pop r1
	pop r0
	rts
	
ft_print_str_top_as_str:
	push r0
	call ft_ds_pop
	call prim_printstr
	pop r0
	rts

ft_setup:
	loadn r7, #FT_DATA_STACK_BEGIN
	loadn r6, #FT_RETURN_STACK_BEGIN
	rts

main:
	call ft_setup
	halt
