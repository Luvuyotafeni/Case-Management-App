<script setup>
  import { ref } from 'vue';
import Header from './Header.vue';

  const documents = ref([]);
  
</script>
<template>
    <div class="container">
        <Header/>
        <div class="dashboard">
            <p> Hello world</p>
        </div>
    </div>
</template>
<style scoped>

.container {
    max-width: 100%;
    height: 100vh;
    display: flex;
    flex-direction: row;
    padding: 0%;
    background-color: #F9FAFB;
  }

  .dashboard{
    display: flex;
    flex-direction: row;
    margin-left: 100px;
    margin-top: 100px;
    width: 100%;
  }
</style>