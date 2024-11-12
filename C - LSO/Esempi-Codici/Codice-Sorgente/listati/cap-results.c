new_perm = ( orig_inh & file_inh ) | ( file_perm & bound_set) ;
new_inh = orig_inh ;
new_eff = file_eff ? new_perm : 0 ;
new_bound_set = bound_set ;
