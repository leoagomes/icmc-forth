; created by the if compiler
jmp ft_emain


; *** VARIABLES ***
FT_CONSOLE_CURSOR_POS: var #1
FT_CONSOLE_COLOR: var #1
FT_DATA_STACK_BEGIN: var #256
FT_RETURN_STACK_BEGIN: var #256

; *** END OF VARIABLES ***


; *** STRINGS ***
STRL_210690925829: string "Test "
STRL_6383562777: string ": OK"
STRL_6951699532315: string ": FAIL"
STRL_4851993666761955123: string "Hello, World"


; *** END OF STRINGS ***


; *** FUNCTIONS ***
; &&& u_eot_fn
u_eot_fn:
call prim_halt
rts

; &&& end of u_eot_fn

; &&& u_begin_test
u_begin_test:
loadn r0, #u_eot_fn
call ft_ds_push
rts

; &&& end of u_begin_test

; &&& u_until_test
u_until_test:
loadn r0, #0
call ft_ds_push
call ft_rs_ds2rs
u_until_test_loop0_begin:
call ft_execute
loadn r0, #STRL_210690925829
call ft_ds_push
call ft_print_top_as_str
call ft_rs_rscpy
call ft_dot
u_until_test_if0_prepare:
call ft_ds_pop
push r1
xor r1, r1, r1
cmp r0, r1
pop r1
jeq u_until_test_if0_after
u_until_test_if0_in:
loadn r0, #STRL_6383562777
call ft_ds_push
jmp u_until_test_if0_after_else
u_until_test_if0_after:
loadn r0, #STRL_6951699532315
call ft_ds_push
u_until_test_if0_after_else:
call ft_print_top_as_str
call ft_cr
call ft_rs_rs2ds
loadn r0, #1
call ft_ds_push
call ft_add
call ft_rs_ds2rs
loadn r0, #0
call ft_ds_push
u_until_test_loop0_puntil:
call ft_ds_pop
push r1
xor r1, r1, r1
cmp r0, r1
pop r1
jeq u_until_test_loop0_begin
u_until_test_loop0_leave:
rts

; &&& end of u_until_test

; &&& u_test_operations
u_test_operations:
loadn r0, #4
call ft_ds_push
call ft_ds_dup
call ft_add
loadn r0, #2
call ft_ds_push
call ft_div
loadn r0, #3
call ft_ds_push
call ft_mul
loadn r0, #5
call ft_ds_push
call ft_sub
loadn r0, #7
call ft_ds_push
call ft_equals
rts

; &&& end of u_test_operations

; &&& u_test_branching_f
u_test_branching_f:
loadn r0, #0
call ft_ds_push
u_test_branching_f_if0_prepare:
call ft_ds_pop
push r1
xor r1, r1, r1
cmp r0, r1
pop r1
jeq u_test_branching_f_if0_after
u_test_branching_f_if0_in:
loadn r0, #0
call ft_ds_push
jmp u_test_branching_f_if0_after_else
u_test_branching_f_if0_after:
loadn r0, #65535
call ft_ds_push
u_test_branching_f_if0_after_else:
rts

; &&& end of u_test_branching_f

; &&& u_test_branching_t
u_test_branching_t:
loadn r0, #65535
call ft_ds_push
u_test_branching_t_if0_prepare:
call ft_ds_pop
push r1
xor r1, r1, r1
cmp r0, r1
pop r1
jeq u_test_branching_t_if0_after
u_test_branching_t_if0_in:
loadn r0, #65535
call ft_ds_push
jmp u_test_branching_t_if0_after_else
u_test_branching_t_if0_after:
loadn r0, #0
call ft_ds_push
u_test_branching_t_if0_after_else:
rts

; &&& end of u_test_branching_t

; &&& u_test_begin_until_leave
u_test_begin_until_leave:
u_test_begin_until_leave_loop0_begin:
loadn r0, #65535
call ft_ds_push
u_test_begin_until_leave_if0_prepare:
call ft_ds_pop
push r1
xor r1, r1, r1
cmp r0, r1
pop r1
jeq u_test_begin_until_leave_if0_after
u_test_begin_until_leave_if0_in:
loadn r0, #65535
call ft_ds_push
jmp u_test_begin_until_leave_loop0_leave
u_test_begin_until_leave_if0_after:
loadn r0, #0
call ft_ds_push
loadn r0, #65535
call ft_ds_push
u_test_begin_until_leave_loop0_puntil:
call ft_ds_pop
push r1
xor r1, r1, r1
cmp r0, r1
pop r1
jeq u_test_begin_until_leave_loop0_begin
u_test_begin_until_leave_loop0_leave:
rts

; &&& end of u_test_begin_until_leave

; &&& u_test_do_loop_leave
u_test_do_loop_leave:
loadn r0, #1
call ft_ds_push
loadn r0, #0
call ft_ds_push
u_test_do_loop_leave_loop0_prepare:
call ft_ds_swap
call ft_rs_ds2rs
call ft_rs_ds2rs
u_test_do_loop_leave_loop0_begin:
loadn r0, #65535
call ft_ds_push
jmp u_test_do_loop_leave_loop0_leave
call ft_ds_drop
loadn r0, #0
call ft_ds_push
u_test_do_loop_leave_loop0_ploop:
call ft_rs_pop
inc r0
push r1
mov r1, r6
dec r1
loadi r1, r1
cmp r0, r1
push fr
call ft_rs_push
pop fr
pop r1
jle u_test_do_loop_leave_loop0_begin
u_test_do_loop_leave_loop0_leave:
dec r6
dec r6
rts

; &&& end of u_test_do_loop_leave

; &&& u_tests
u_tests:
loadn r0, #STRL_4851993666761955123
call ft_ds_push
call ft_print_top_as_str
call u_begin_test
loadn r0, #u_test_operations
call ft_ds_push
loadn r0, #u_test_branching_f
call ft_ds_push
loadn r0, #u_test_branching_t
call ft_ds_push
loadn r0, #u_test_begin_until_leave
call ft_ds_push
loadn r0, #u_test_do_loop_leave
call ft_ds_push
call u_until_test
rts

; &&& end of u_tests

; &&& ft_emain
ft_emain:
call ft_setup
call u_tests
halt

; &&& end of ft_emain

; &&& prim_halt
prim_halt:
    halt
; &&& end of prim_halt

; &&& ft_ds_push
; r0 -- data to push
; ( -- <r0> )
ft_ds_push:
	storei r7, r0
	inc r7
	rts
; &&& end of ft_ds_push

; &&& ft_ds_pop
; r0 -- data popped
; ( a -- ) r0 = a
ft_ds_pop:
	dec r7
	loadi r0, r7
	rts
; &&& end of ft_ds_pop

; &&& ft_rs_push
; r0 -- data to push
ft_rs_push:
	storei r6, r0
	inc r6
	rts
; &&& end of ft_rs_push

; &&& ft_rs_ds2rs
; ( a -- ) { -- a }
ft_rs_ds2rs:
	push r0
	call ft_ds_pop
	call ft_rs_push
	pop r0
	rts
; &&& end of ft_rs_ds2rs

; &&& ft_execute
ft_execute:
    push r0
    push r1
    loadn r1, #ft_execute_call
    inc r1
    call ft_ds_pop
    storei r1, r0
ft_execute_call:
    call ft_execute_aftercall
ft_execute_aftercall:
    pop r1
    pop r0
    rts
; &&& end of ft_execute

; &&& prim_printstr
; r0 : str
prim_printstr:
	push r0
	push r1
	push r2
	push r3
	push r4

	load r4, FT_CONSOLE_COLOR
	xor r2, r2, r2
	load r1, FT_CONSOLE_CURSOR_POS

prim_printstr_loop:
	loadi r3, r0
	inc r1
	cmp r3, r2
	jeq prim_printstr_loop_end
	add r3, r3, r4             ; add color to char
	outchar r3, r1
	inc r0
	jmp prim_printstr_loop

prim_printstr_loop_end:
    store FT_CONSOLE_CURSOR_POS, r1
	pop r4
	pop r3
	pop r2
	pop r1
	pop r0
	rts
; &&& end of prim_printstr

; &&& ft_print_top_as_str
; ( addrs -- )
ft_print_top_as_str:
	push r0
	call ft_ds_pop
	call prim_printstr
	pop r0
	rts
; &&& end of ft_print_top_as_str

; &&& ft_rs_rscpy
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
; &&& end of ft_rs_rscpy

; &&& prim_printno
FT_NUMSTR_BUFFER: var #5
FT_NUMSTR_BUFFER_END: var #2

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
	div r0, r0, r3
	cmp r0, r5
	jne prim_printno_loop

	inc r2
	mov r0, r2

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
; &&& end of prim_printno

; &&& ft_dot
ft_dot:
	push r0

	call ft_ds_pop
	call prim_printno
	load r0, FT_CONSOLE_CURSOR_POS
	inc r0
	store FT_CONSOLE_CURSOR_POS, r0

	pop r0
	rts

; &&& end of ft_dot

; &&& prim_cr
prim_cr:
	push r0
	push r1
	push r2

	loadn r1, #40
	load r0, FT_CONSOLE_CURSOR_POS
	div r2, r0, r1
	inc r2
	mul r2, r2, r1
	loadn r0, #1200
	mod r2, r2, r0
	store FT_CONSOLE_CURSOR_POS, r2

	pop r2
	pop r1
	pop r0
	rts

; &&& end of prim_cr

; &&& prim_clearline
prim_clearline:
	push r0
	push r1
	push r2

	load r0, FT_CONSOLE_CURSOR_POS
	loadn r1, #40
	div r2, r0, r1
	mul r2, r2, r1
	add r1, r1, r2
	loadn r0, #' '

prim_clearline_lp:
	outchar r0, r2
	inc r2
	cmp r2, r1
	jle prim_clearline_lp

	pop r0
	pop r1
	pop r2
	rts

; &&& end of prim_clearline

; &&& ft_cr
ft_cr:
	call prim_cr
	call prim_clearline
	rts

; &&& end of ft_cr

; &&& ft_rs_pop
; r0 -- data popped
ft_rs_pop:
	dec r6
	loadi r0, r6
	rts
; &&& end of ft_rs_pop

; &&& ft_rs_rs2ds
; ( -- a ) { a -- }
ft_rs_rs2ds:
	push r0
	call ft_rs_pop
	call ft_ds_push
	pop r0
	rts
; &&& end of ft_rs_rs2ds

; &&& ft_add
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
; &&& end of ft_add

; &&& ft_ds_dup
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
; &&& end of ft_ds_dup

; &&& ft_div
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
; &&& end of ft_div

; &&& ft_mul
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
; &&& end of ft_mul

; &&& ft_sub
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
; &&& end of ft_sub

; &&& ft_equals
ft_equals:
	push r0
	push r1
	
	call ft_ds_pop
	mov r1, r0
	call ft_ds_pop
	cmp r1, r0
	jne ft_equals_push0
	loadn r0, #1
	jmp ft_equals_end
ft_equals_push0:
	xor r0, r0, r0

ft_equals_end:
	call ft_ds_push
	pop r1
	pop r0
	rts
; &&& end of ft_equals

; &&& ft_ds_swap
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

; &&& end of ft_ds_swap

; &&& ft_ds_drop
; ( a -- )
ft_ds_drop:
	dec r7
	rts

; &&& end of ft_ds_drop

; &&& ft_setup
ft_setup:
	loadn r7, #FT_DATA_STACK_BEGIN
	loadn r6, #FT_RETURN_STACK_BEGIN
	rts
; &&& end of ft_setup



; *** END OF FUNCTIONS ***


; *** STATIC ***

; *** END OF STATIC ***
FT_HERE_LABEL: