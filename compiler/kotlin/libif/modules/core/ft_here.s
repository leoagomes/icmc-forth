ft_here:
    push r0
    loadn r0, #FT_HERE_LABEL
    call ft_ds_push
    pop r0
    rts