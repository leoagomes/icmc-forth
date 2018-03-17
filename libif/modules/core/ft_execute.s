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