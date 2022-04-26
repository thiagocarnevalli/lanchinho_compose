package com.empthi.composelanchinho.domain.interfaces

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class MenuStateImpl() : MenuState {
    override val state: MutableStateFlow<MenuState.State> =
        MutableStateFlow(MenuState.State.Initial)
}

class MenuActionImpl() : MenuAction {
    override val action = MutableSharedFlow<MenuAction.Action>()
}