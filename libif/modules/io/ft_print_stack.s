; prints the stack
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